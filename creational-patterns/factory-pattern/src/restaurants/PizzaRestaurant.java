package restaurants;

import pizza.Pizza;

public abstract class PizzaRestaurant {

    abstract Pizza createPizza(String type);

    public Pizza orderPizza(String type){
        Pizza pizza = createPizza(type);
        pizza.prepare();
        return pizza;
    }
}
