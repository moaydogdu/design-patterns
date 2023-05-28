import restaurants.AnkaraPizzaRestaurant;
import restaurants.KocaeliPizzaRestaurant;
import restaurants.PizzaRestaurant;

public class Main {
    public static void main(String[] args) {
        PizzaRestaurant kocaeliPizzaRestaurant = new KocaeliPizzaRestaurant();
        kocaeliPizzaRestaurant.orderPizza("Pepperoni");
        PizzaRestaurant ankaraPizzaRestaurant = new AnkaraPizzaRestaurant();
        ankaraPizzaRestaurant.orderPizza("Cheese");
        ankaraPizzaRestaurant.orderPizza("Veggie");
    }
}