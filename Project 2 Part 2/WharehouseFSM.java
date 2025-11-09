public class WarehouseFSM {
    public static void main(String[] args) {
        Context context = new Context();
        context.setState(new OpeningState());
        context.run();
    }
}
