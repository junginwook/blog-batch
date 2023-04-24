package blog.study.top.repository.pass;

import java.util.Random;
import org.jeasy.random.api.Randomizer;

public class CustomStatusRandomizer implements Randomizer<String> {

	private static Random random = new Random();

	@Override
	public String getRandomValue() {
		PassStatus[] values = PassStatus.values();
		PassStatus value = values[random.nextInt(3)];
		return value.name();
	}
}
