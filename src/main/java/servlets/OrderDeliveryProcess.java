package servlets;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import connectors.Persistence;
import fetcher.DataFetcher;

public class OrderDeliveryProcess {
	
	private int processId;
	
	private int orderId;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getProcessId() {
		return processId;
	}

	public void setProcessId(int processId) {
		this.processId = processId;
	}

	public void processOrderFlow() {
        // Receiving orders
		
        receiveOrders()
        .thenComposeAsync(orders -> {
        	return beforeReceiveOrders(orders);})
            // Order processing
            .thenComposeAsync(orders -> {
            	updateOrderProcess("receivetime","The order is picked");
            	return processOrders(orders);})
            // Being delivered
            .thenComposeAsync(orders -> {
            	updateOrderProcess("delivertime","The order is delivertime");
            	return deliverOrders(orders);})
            // Delivered
            .thenAccept(orders -> {
            	markOrdersDelivered(orders);
            	updateTheOrder();
            	updateOrderProcess("deliveredtime","The order is deliveredtime");
            })
            // Error handling
            .exceptionally(ex -> {
                System.err.println("Error in order delivery process: " + ex.getMessage());
                return null;
            });
    }

    private CompletableFuture<String> receiveOrders() {
        System.out.println("Receiving orders");
        // Simulate receiving orders
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2); // 2 seconds delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return ""; // Sample orders
        });
    }
    
    private CompletableFuture<String> beforeReceiveOrders(String orders) {
        System.out.println("Being delivered");
        // Simulate order being delivered
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1000); // 4 seconds delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return orders;
        });
    }

    private CompletableFuture<String> processOrders(String orders) {
        System.out.println("Order processing");
        // Simulate order processing
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(300); // 3 seconds delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return orders;
        });
    }

    private CompletableFuture<String> deliverOrders(String orders) {
        System.out.println("Being delivered");
        // Simulate order being delivered
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(400); // 4 seconds delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return orders;
        });
    }

    private void markOrdersDelivered(String orders) {
        System.out.println("Delivered");
        // Simulate marking orders as delivered
    }
    
    public void updateOrderProcess(String columnName,String description) {
    	DataFetcher datafetcher = new Persistence().getDatafetcher();
    	try {
    	String updateQuery = "update orderprocess set "+columnName+" = '"+LocalDateTime.now()+"', description ='"+description+"' where id = "+this.processId+";";
    	datafetcher.executeUpdateQuery("orderprocess", updateQuery);
    	}
    	catch (Exception e) {
			// TODO: handle exception
		}
    }
    
    public void updateTheOrder() {
    	DataFetcher datafetcher = new Persistence().getDatafetcher();
    	try {
    	String updateQuery = "update orderdetails set orderstatus = 'complate' where id = "+orderId+";";
    	datafetcher.executeUpdateQuery("orderdetails", updateQuery);
    	}
    	catch (Exception e) {
			// TODO: handle exception
		}
    }
	
}
