package com.demo.testing.rxjavaschedulers

import io.reactivex.Scheduler
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class RxSchedulerOverrides
@JvmOverloads constructor(
  private val ioScheduler: Scheduler = Schedulers.trampoline(),
  private val computationScheduler: Scheduler = Schedulers.trampoline()
) : TestRule {
  override fun apply(base: Statement, description: Description): Statement =
    object : Statement() {
      override fun evaluate() {
        RxJavaPlugins.setIoSchedulerHandler { ioScheduler }
        RxJavaPlugins.setComputationSchedulerHandler { computationScheduler }
        try {
          base.evaluate()
        } finally {
          RxJavaPlugins.reset()
        }
      }
    }
}