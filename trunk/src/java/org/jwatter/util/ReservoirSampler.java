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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Samples a specified number of elements from a sequence of elements with uniform distribution.
 * Elements are presented to the sampler one by one, and the total number of elements is not known.
 * At any time, after m>n elements have been presented, where n is the requested sample size, a
 * sample of n elements is available. When the m-th element is presented, where m>n, one of the
 * elements in the sample is replaced with the new one with probability n/m. If no more than n
 * elements are presented, all of them are included in the sample.
 * 
 * See <a href="http://en.wikipedia.org/wiki/Reservoir_sampling">http://en.wikipedia.org/wiki/
 * Reservoir_sampling</a>.
 */
public class ReservoirSampler<T> {

    protected int sampleSize;
    protected ArrayList<T> sample;
    protected int inputSize;
    protected Random random;

    /**
     * Creates a new reservoir sampler with the specified sample size.
     * 
     * @param size
     *        the sample size
     */
    public ReservoirSampler (int size) {
        if (size < 1) {
            throw new IllegalArgumentException("sample size must be greater than zero");
        }
        this.sampleSize = size;
        this.sample = new ArrayList<T>();
        this.inputSize = 0;
        this.random = new Random();
    }

    /**
     * Returns the requested sample size.
     * 
     * @return the requested sample size
     */
    public int getSampleSize () {
        return this.sampleSize;
    }

    /**
     * Returns the actual sample size. The actual sample size is n if n is the requested sample size
     * and m is the number of elements read so far and n>=m, and m if m<n.
     * 
     * @return the actual sample size
     */
    public int getCurrentSampleSize () {
        return this.sample.size();
    }

    /**
     * Returns the number of elements read so far.
     * 
     * @return the number of elements read
     */
    public int getInputSize () {
        return this.inputSize;
    }

    /**
     * Returns the current sample. The returned list is a new instance, but its elements are the
     * elements read by the sampler.
     * 
     * @return the current sample
     */
    @SuppressWarnings("unchecked")
    public List<T> getSample () {
        return (List<T>)this.sample.clone();
    }

    /**
     * Presents the next element to the sampler.
     * 
     * @param element
     *        the next element in the sequence to sample from
     */
    public void read (T element) {
        this.inputSize++;
        if (this.sample.size() < this.sampleSize) {
            this.sample.add(element);
        }
        else {
            int p = random.nextInt(inputSize);
            if (p < this.sampleSize) {
                this.sample.set(p, element);
            }
        }
    }
}
