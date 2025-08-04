package com.example.ecommerce.service;






import com.example.ecommerce.dto.cart.OrderItemResponse;
import com.example.ecommerce.dto.cart.OrderResponse;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.OrderItem;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.mapper.CartMapper;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CartService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    private Order getOrCreatePendingOrder(User user){
        return orderRepository.findByUserAndStatus(user, Order.OrderStatus.PENDING).orElseGet(() ->{
            Order order = Order.builder()
                          .user(user)
                          .status(Order.OrderStatus.PENDING)
                          .totalAmount(0.0)
                          .build();
            return orderRepository.save(order);
        });
    }

    private void recalculateTotal(Order order){
        double total = order.getOrderItems().stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        order.setTotalAmount(total);
    }

    private void validateCartItems(List<OrderItem> items){
        for (OrderItem item : items){
            Product product = item.getProduct();
            Integer quantity = item.getQuantity();

            if (quantity == null || quantity <= 0) {
                throw new RuntimeException("Invalid quantity for product: " + product.getName());
            }

            if (product.getStock() < quantity) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
        }
    }

    private void updateProductStock(List<OrderItem> items) {
        for (OrderItem item : items) {
            Product product = item.getProduct();
            int newStock = product.getStock() - item.getQuantity();
            product.setStock(newStock);
            productRepository.save(product);
        }
    }




    @Transactional
    public Order addToCart(String username, Long productId, Integer quantity){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("user not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        Order order = getOrCreatePendingOrder(user);

        OrderItem existingItem = order.getOrderItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null){
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            OrderItem item = OrderItem.builder().product(product).quantity(quantity).price(product.getPrice()).build();
            order.addOrderItem(item);
        }


        recalculateTotal(order);

        return orderRepository.save(order);

    }


    @Transactional
    public Order removeItemFromCart(String username, Long productId){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not Found"));

        Order order = orderRepository.findByUserAndStatus(user, Order.OrderStatus.PENDING).orElseThrow(() -> new RuntimeException("Cart is empty"));

        OrderItem target =  order.getOrderItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));
        order.removeOrderItem(target);

        recalculateTotal(order);


        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public OrderResponse getCart(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not Found"));

        Order order = orderRepository.findByUserAndStatus(user, Order.OrderStatus.PENDING).orElseThrow(() -> new RuntimeException("Cart is empty"));

        List<OrderItem> pagedItems = order.getOrderItems()
                .stream()
                .skip((long) pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .toList();

        List<OrderItemResponse> itemResponses = pagedItems
                .stream()
                .map(cartMapper::toOrderItemResponse)
                .toList();

        return cartMapper.toPageOrderResponse(order, itemResponses);
    }
    @Transactional
    public Order checkout(String username){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not Found"));

         Order order =  orderRepository.findByUserAndStatus(user, Order.OrderStatus.PENDING).orElseThrow(() -> new RuntimeException("Cart is empty"));

         List<OrderItem> items = order.getOrderItems();

        if (items.isEmpty()){
            throw  new RuntimeException("No item in cart");
        }
        validateCartItems(items);
        updateProductStock(items);

        order.setStatus(Order.OrderStatus.WAITING_PAYMENT);
        order.setCreatedAt(LocalDateTime.now());
      return orderRepository.save(order);

    }

    @Transactional
    public Order clearCart(String username){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not Found"));

        Order order = orderRepository.findByUserAndStatus(user, Order.OrderStatus.PENDING).orElseThrow(() -> new RuntimeException("Cart is empty"));

        order.getOrderItems().clear();
        order.setTotalAmount(0.0);
        return orderRepository.save(order);
    }

    @Transactional
    public Order processPaymentAfter(String username, Order.PaymentMethod method){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not Found"));

        Order order =  orderRepository.findByUserAndStatus(user, Order.OrderStatus.WAITING_PAYMENT).orElseThrow(() -> new RuntimeException("No Pending payment found"));


        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to pay this order");
        }

        if (!order.getStatus().equals(Order.OrderStatus.WAITING_PAYMENT)) {
            throw new RuntimeException("Order is not awaiting payment");
        }

        if (method == null ){
            throw new RuntimeException("Payment method must be provided");
        }
        order.setStatus(Order.OrderStatus.PAID);
        order.setPaymentMethod(method);
        order.setUpdateAt(LocalDateTime.now());

        return orderRepository.save(order);
    }

    @Transactional
    public Order processPayment(String username, Long orderId, Order.PaymentMethod method) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to pay this order");
        }

        if (!order.getStatus().equals(Order.OrderStatus.WAITING_PAYMENT)) {
            throw new RuntimeException("This order cannot be paid");
        }

        if (method == null) {
            throw new RuntimeException("Payment method is required");
        }

        order.setStatus(Order.OrderStatus.PAID);
        order.setPaymentMethod(method);
        order.setUpdateAt(LocalDateTime.now());

        return orderRepository.save(order);
    }


    // History, exception handling buat cart,


}
