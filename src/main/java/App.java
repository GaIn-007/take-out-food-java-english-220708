import java.util.*;

class Food extends Item {
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

        List<Item> item_list = this.itemRepository.findAll();

        Map<String, Food> order = new HashMap<>();

        double total_price = 0.0d;
        String res_str = "";
        res_str += "============= Order details =============\n";

        for (String tmp : inputs) {
            String[] tmp_food = tmp.split(" x ");
            String tmp_id = tmp_food[0];
            int tmp_amount = Integer.parseInt(tmp_food[1]);
            for (Item item : item_list) {
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

        List<SalesPromotion> sales_promotion_list = this.salesPromotionRepository.findAll();
        List<String> discount_items = new ArrayList<>();
        double discount_price = 0.0d;
        for (String tmp2 : inputs) {
            String[] tmp_food2 = tmp2.split(" x ");
            String tmp_id2 = tmp_food2[0];
            for (SalesPromotion sales_promotion : sales_promotion_list) {
                if (sales_promotion.getType().equals("50%_DISCOUNT_ON_SPECIFIED_ITEMS")) {
                    for (String related_item : sales_promotion.getRelatedItems()) {
                        if (related_item.equals(tmp_id2)) {
                            Food food2 = order.get(related_item);
                            discount_items.add(food2.getName());
                            discount_price += food2.getPrice() * food2.getAmount() / 2;
                        }
                    }
                }
            }
        }

        if (discount_price > 6) {
            res_str += "Promotion used:\n";
            res_str += String.format("Half price for certain dishes (%s), saving %d yuan\n", String.join(" and ", discount_items), (int) discount_price);
            res_str += "-----------------------------------\n";
            res_str += String.format("Total: %d yuan\n", (int) (total_price - discount_price));
            res_str += "===================================";
        } else if (total_price >= 30 && discount_price <= 6) {
            discount_price = 6;
            res_str += "Promotion used:\n";
            res_str += String.format("Deduct 6 yuan when the order reaches 30 yuan, saving %d yuan\n", (int) discount_price);
            res_str += "-----------------------------------\n";
            res_str += String.format("In total: %d yuan\n", (int) (total_price - discount_price));
            res_str += "===================================";
        } else if (total_price < 30 && discount_price > 0) {
            res_str += "Promotion used:\n";
            res_str += String.format("Half price for certain dishes (%s), saving %d yuan\n", String.join(" and ", discount_items), (int) discount_price);
            res_str += "-----------------------------------\n";
            res_str += String.format("Total: %d yuan\n", (int) (total_price - discount_price));
            res_str += "===================================";
        } else {
            res_str += String.format("In total: %d yuan\n", (int) total_price);
            res_str += "===================================";
        }
        return res_str;
    }
}
