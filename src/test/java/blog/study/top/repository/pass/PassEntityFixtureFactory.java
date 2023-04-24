package blog.study.top.repository.pass;

import static org.jeasy.random.FieldPredicates.inClass;
import static org.jeasy.random.FieldPredicates.named;
import static org.jeasy.random.FieldPredicates.ofType;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.function.Predicate;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.randomizers.range.IntegerRangeRandomizer;
import org.jeasy.random.randomizers.range.LocalDateRangeRandomizer;
import org.jeasy.random.randomizers.range.LongRangeRandomizer;
import org.jeasy.random.randomizers.text.StringRandomizer;

public class PassEntityFixtureFactory {

	public static EasyRandom get(LocalDate start, LocalDate end) {
		EasyRandomParameters parameter = getEasyRandomParameters();
		parameter
				.randomize(userId(), new StringRandomizer())
				.randomize(remainingCount(), new IntegerRangeRandomizer(0, 50))
				.randomize(passStatus(), new CustomStatusRandomizer())
				.randomize(createdAt(), new LocalDateRangeRandomizer(start, end))
				.randomize(modifiedAt(), new LocalDateRangeRandomizer(start, end))
				.randomize(endedAt(), new LocalDateRangeRandomizer(start, end))
				.randomize(expiredAt(), new LocalDateRangeRandomizer(start, end))
				.randomize(startedAt(), new LocalDateRangeRandomizer(start, end));

		return new EasyRandom(parameter);
	}

	private static EasyRandomParameters getEasyRandomParameters() {
		return new EasyRandomParameters()
				.excludeField(named("passSeq"))
				.excludeField(named("packageSeq"))
				.stringLengthRange(1, 10)
				.randomize(Long.class, new LongRangeRandomizer(1L, 100000L));
	}

	private static Predicate<Field> userId() {
		return named("userId").and(ofType(String.class)).and(inClass(PassDto.class));
	}

	private static Predicate<Field> passStatus() {
		return named("passStatus").and(ofType(String.class)).and(inClass(PassDto.class));
	}

	private static Predicate<Field> remainingCount() {
		return named("remainingCount").and(ofType(Integer.class)).and(inClass(PassDto.class));
	}

	private static Predicate<Field> createdAt() {
		return named("createdAt").and(ofType(LocalDate.class)).and(inClass(PassDto.class));
	}

	private static Predicate<Field> modifiedAt() {
		return named("modifiedAt").and(ofType(LocalDate.class)).and(inClass(PassDto.class));
	}

	private static Predicate<Field> endedAt() {
		return named("endedAt").and(ofType(LocalDate.class)).and(inClass(PassDto.class));
	}

	private static Predicate<Field> startedAt() {
		return named("startedAt").and(ofType(LocalDate.class)).and(inClass(PassDto.class));
	}

	private static Predicate<Field> expiredAt() {
		return named("expiredAt").and(ofType(LocalDate.class)).and(inClass(PassDto.class));
	}
}
