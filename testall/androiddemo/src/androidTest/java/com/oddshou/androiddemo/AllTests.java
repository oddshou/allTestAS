/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oddshou.androiddemo;

import junit.framework.Test;
import junit.framework.TestSuite;

import android.test.suitebuilder.TestSuiteBuilder;

/**
 * A test suite containing all tests for ApiDemos.
 *
 * To run all suites found in this apk:
 * $ adb shell am instrument -w \
 *   com.oddshou.androiddemo.tests/android.test.InstrumentationTestRunner
 *
 * To run just this suite from the command line:
 * $ adb shell am instrument -w \
 *   -e class com.oddshou.androiddemo.AllTests \
 *   com.oddshou.androiddemo.tests/android.test.InstrumentationTestRunner
 *
 * To run an individual test case, e.g. {@link com.oddshou.androiddemo.os.MorseCodeConverterTest}:
 * $ adb shell am instrument -w \
 *   -e class com.oddshou.androiddemo.os.MorseCodeConverterTest \
 *   com.oddshou.androiddemo.tests/android.test.InstrumentationTestRunner
 *
 * To run an individual test, e.g. {@link com.oddshou.androiddemo.os.MorseCodeConverterTest#testCharacterS()}:
 * $ adb shell am instrument -w \
 *   -e class com.oddshou.androiddemo.os.MorseCodeConverterTest#testCharacterS \
 *   com.oddshou.androiddemo.tests/android.test.InstrumentationTestRunner
 */
public class AllTests extends TestSuite {

    public static Test suite() {
        return new TestSuiteBuilder(AllTests.class)
                .includeAllPackagesUnderHere()
                .build();
    }
}
