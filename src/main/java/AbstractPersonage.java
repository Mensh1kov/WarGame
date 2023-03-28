public abstract class AbstractPersonage implements Personage {
    protected Double x;
    protected Double y;
    protected Double speed = 10D;
    protected Long lastTimeMove = -1L;
    private Double last_x = 0D;
    private Double last_y = 0D;

    @Override
    public void move(Double x, Double y) {
        if (System.currentTimeMillis() - lastTimeMove >= speed || last_x != x || last_y != y) {
            this.x += x;
            this.y += y;
            this.lastTimeMove = System.currentTimeMillis();
        }
    }

    @Override
    public Double getX() {
        return this.x;
    }

    @Override
    public Double getY() {
        return this.y;
    }
}
