import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Food extends Item{
    private int amount;

    public Food(String id, String name, double price) {
        super(id, name, price);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

public class App {
    private ItemRepository itemRepository;
    private SalesPromotionRepository salesPromotionRepository;

    public App(ItemRepository itemRepository, SalesPromotionRepository salesPromotionRepository) {
        this.itemRepository = itemRepository;
        this.salesPromotionRepository = salesPromotionRepository;
    }

    public String bestCharge(List<String> inputs) {
        String res_str = "";
        List<Item> item_list = this.itemRepository.findAll();
        List<SalesPromotion> sales_promotion_list = this.salesPromotionRepository.findAll();

        Map<String, Food> order = new HashMap<>();
        double total_price = 0;
        res_str += "============= Order details =============\n";

        for (String tmp: inputs){
            String[] tmp_food= tmp.split(" x ");
            String tmp_id = tmp_food[0];
            int tmp_amount = Integer.parseInt(tmp_food[1]);
            for (Item item: item_list) {
                if (item.getId().equals(tmp_id)) {
                    Food food = new Food(tmp_id, item.getName(), item.getPrice());
                    food.setAmount(tmp_amount);
                    order.put(tmp_id, food);
                    double tmp_price = item.getPrice() * tmp_amount;
                    total_price += tmp_price;
                    res_str += String.format("%s x %s = %d yuan\n", item.getName(), tmp_amount, (int) tmp_price);
                    break;
                }
            }
        }
        res_str += "-----------------------------------\n";
        System.out.println(res_str);

        

        return null;
    }
}
