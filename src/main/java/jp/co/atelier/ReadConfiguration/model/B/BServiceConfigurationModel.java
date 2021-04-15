package jp.co.atelier.ReadConfiguration.model.B;

public class BServiceConfigurationModel {

	private float height;
	private float weight;
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public float getWeight() {
		return weight;
	}
	public void setWeight(float weight) {
		this.weight = weight;
	}
	@Override
	public String toString() {
		return "BServiceConfigurationModel [height=" + height + ", weight=" + weight + "]";
	}
}
