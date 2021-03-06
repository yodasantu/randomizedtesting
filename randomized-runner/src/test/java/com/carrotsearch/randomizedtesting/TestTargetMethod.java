package com.carrotsearch.randomizedtesting;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.carrotsearch.randomizedtesting.annotations.Repeat;

public class TestTargetMethod extends WithNestedTestClass {
  public static class Nested extends RandomizedTest {
    @Before @After
    public void checkInHook() {
      assumeRunningNested();
      Assertions.assertThat(RandomizedContext.current().getTargetMethod()).isNotNull();
    }

    @Test
    @Repeat(iterations = 3)
    public void testOne() {
      Assertions.assertThat(RandomizedContext.current().getTargetMethod().getName())
        .isEqualTo("testOne");
    }
    
    @Test
    public void testTwo() {
      Assertions.assertThat(RandomizedContext.current().getTargetMethod().getName())
        .isEqualTo("testTwo");
    }
    
    @AfterClass
    @BeforeClass
    public static void staticHooks() {
      Assertions.assertThat(RandomizedContext.current().getTargetMethod())
        .isNull();
    }
  }

  @Test
  public void testTargetMethodAvailable() {
    Result result = JUnitCore.runClasses(Nested.class);
    Assertions.assertThat(result.getFailureCount()).isEqualTo(0);
    Assertions.assertThat(result.getRunCount()).isEqualTo(4);
  }
}
