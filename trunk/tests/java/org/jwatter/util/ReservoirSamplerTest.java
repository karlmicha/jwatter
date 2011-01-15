/*
Copyright 2011 Karl-Michael Schneider

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.jwatter.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

public class ReservoirSamplerTest {

    protected static final int MAX_DATA_SIZE = 1000;

    // variables for testing sample uniformity
    protected static final int UNIFORMITY_DATA_SIZE = 100000;
    protected static final int UNIFORMITY_DATA_RANGE = 100;
    protected static final int UNIFORMITY_SAMPLE_SIZE = 10000;
    protected static final double UNIFORMITY_ALLOWED_DEVIATION = 0.005;

    protected static final Logger logger = Logger.getLogger(ReservoirSamplerTest.class.getName());

    protected ReservoirSampler<Integer> sampler;
    protected Random random;
    protected Integer[] data;

    @Before
    public void setUp () {
        random = new Random();
        data = new Integer[random.nextInt(MAX_DATA_SIZE)];
        logger.info("data size = " + data.length);
        for (int i = 0; i < data.length; i++) {
            data[i] = random.nextInt();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalSampleSize () {
        sampler = new ReservoirSampler<Integer>(0);
    }

    @Test
    public void testGetSampleSize () {
        sampler = new ReservoirSampler<Integer>(data.length);
        assertEquals(data.length, sampler.getSampleSize());
    }

    @Test
    public void testGetCurrentSampleSizeReadLess () {
        if (data.length == 0)
            return;
        sampler = new ReservoirSampler<Integer>(data.length);
        int inputSize = random.nextInt(data.length + 1);
        readFirstNElements(inputSize);
        assertEquals(inputSize, sampler.getCurrentSampleSize());
    }

    @Test
    public void testGetCurrentSampleSizeReadMore () {
        if (data.length < 2)
            return;
        int sampleSize = random.nextInt(data.length - 1) + 1;
        sampler = new ReservoirSampler<Integer>(sampleSize);
        readFirstNElements(data.length);
        assertEquals(sampleSize, sampler.getCurrentSampleSize());
    }

    @Test
    public void testGetInputSize () {
        if (data.length == 0)
            return;
        sampler = new ReservoirSampler<Integer>(data.length);
        int inputSize = random.nextInt(data.length + 1);
        readFirstNElements(inputSize);
        assertEquals(inputSize, sampler.getInputSize());
    }

    @Test
    public void testSampleAll () {
        if (data.length == 0)
            return;
        sampler = new ReservoirSampler<Integer>(data.length);
        readFirstNElements(data.length);
        testFirstNElements(data.length);
    }

    @Test
    public void testSampleLess () {
        if (data.length == 0)
            return;
        sampler = new ReservoirSampler<Integer>(data.length);
        int inputSize = random.nextInt(data.length);
        readFirstNElements(inputSize);
        testFirstNElements(inputSize);
    }

    @Test
    public void testSample () {
        if (data.length < 2)
            return;
        int sampleSize = random.nextInt(data.length - 1) + 1;
        sampler = new ReservoirSampler<Integer>(sampleSize);
        readFirstNElements(data.length);
        Set<Integer> input = new HashSet<Integer>();
        input.addAll(Arrays.asList(data));
        for (Integer e : sampler.getSample()) {
            assertTrue("sample element not in original data", input.contains(e));
        }
    }

    @Test
    public void testUniformDistribution () {
        sampler = new ReservoirSampler<Integer>(UNIFORMITY_SAMPLE_SIZE);
        for (int p = 0; p < UNIFORMITY_DATA_SIZE; p++) {
            sampler.read(random.nextInt(UNIFORMITY_DATA_RANGE));
        }
        int[] counts = new int[UNIFORMITY_DATA_RANGE];
        Arrays.fill(counts, 0);
        for (Integer e : sampler.getSample()) {
            counts[e]++;
        }
        double uniformprob = 1.0 / UNIFORMITY_DATA_RANGE;
        for (int e = 0; e < UNIFORMITY_DATA_RANGE; e++) {
            double prob = ((double)counts[e]) / UNIFORMITY_SAMPLE_SIZE;
            assertEquals(e + " undersampled", uniformprob, prob, UNIFORMITY_ALLOWED_DEVIATION);
        }
    }

    protected void readFirstNElements (int n) {
        for (int i = 0; i < n; i++) {
            sampler.read(data[i]);
        }
    }

    protected void testFirstNElements (int n) {
        Iterator<Integer> sample = sampler.getSample().iterator();
        for (int i = 0; i < n; i++) {
            assertTrue("sample too small", sample.hasNext());
            assertEquals(data[i], sample.next());
        }
        assertFalse("sample too big", sample.hasNext());
    }
}
