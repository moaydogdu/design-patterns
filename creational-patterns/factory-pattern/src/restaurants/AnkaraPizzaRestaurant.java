package restaurants;

import pizza.Pizza;
import pizza.ankaraStyle.AnkaraStyleCheesePizza;
import pizza.ankaraStyle.AnkaraStylePepperoniPizza;
import pizza.ankaraStyle.AnkaraStyleVeggiePizza;
import pizza.kocaeliStyle.KocaeliStylePepperoniPizza;
import pizza.kocaeliStyle.KocaeliStyleVeggiePizza;

public class AnkaraPizzaRestaurant extends PizzaRestaurant{

    @Override
    Pizza createPizza(String type) {
        Pizza pizza = null;

        if(type.equalsIgnoreCase("Pepperoni")){
            pizza = new AnkaraStylePepperoniPizza();
        } else if (type.equalsIgnoreCase("Veggie")){
            pizza = new AnkaraStyleVeggiePizza();
        } else if(type.equalsIgnoreCase("Cheese")){
            pizza = new AnkaraStyleCheesePizza();
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
