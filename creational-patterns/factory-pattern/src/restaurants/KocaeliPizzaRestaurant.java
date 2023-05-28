package restaurants;

import pizza.Pizza;
import pizza.kocaeliStyle.KocaeliStylePepperoniPizza;
import pizza.kocaeliStyle.KocaeliStyleVeggiePizza;
import restaurants.PizzaRestaurant;

public class KocaeliPizzaRestaurant extends PizzaRestaurant {
    @Override
    Pizza createPizza(String type) {
        Pizza pizza = null;

        if(type.equalsIgnoreCase("Pepperoni")){
            pizza = new KocaeliStylePepperoniPizza();
        } else if (type.equalsIgnoreCase("Veggie")){
            pizza = new KocaeliStyleVeggiePizza();
        }
        return pizza;
    }

    @Override
    public Pizza orderPizza(String type) {
        Pizza pizza = createPizza(type);
        //ordering...
        pizza.prepare();
        return pizza;
    }
}
