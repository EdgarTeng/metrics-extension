package com.tenchael.metrics.extension.metrics;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SlidingWindowReservoirTests {
	private final SlidingWindowReservoir reservoir = new SlidingWindowReservoir(3);

	@Test
	public void testConstruct() throws Exception {
		SlidingWindowReservoir resv = new SlidingWindowReservoir();
		assertThat(resv.size() == 1028);
	}

	@Test
	public void handlesSmallDataStreams() throws Exception {
		reservoir.update(1);
		reservoir.update(2);

		assertThat(reservoir.getSnapshot().getValues())
				.containsOnly(1, 2);
	}

	@Test
	public void onlyKeepsTheMostRecentFromBigDataStreams() throws Exception {
		reservoir.update(1);
		reservoir.update(2);
		reservoir.update(3);
		reservoir.update(4);

		assertThat(reservoir.getSnapshot().getValues())
				.containsOnly(2, 3, 4);
	}
}
