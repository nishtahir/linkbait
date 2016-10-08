package com.nishtahir.linkbait.commons

import com.machinepublishers.jbrowserdriver.JBrowserDriver
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object BrowserPoolExecutor {

    /**
     * Maximum number of idle browsers to remain in pool
     */
    private val MAX_IDLE = 3

    /**
     * Maximum number of instances
     */
    private val MAX_TOTAL = 5

    private var browserPool: BrowserPool

    /**
     *
     */
    private var executor: ExecutorService

    init {
        val config = GenericObjectPoolConfig().apply {
            maxIdle = MAX_IDLE
            maxTotal = MAX_TOTAL
        }

        browserPool = BrowserPool(BrowserFactory(), config)
        executor = ThreadPoolExecutor(10, 10, 0L,
                TimeUnit.MILLISECONDS,
                ArrayBlockingQueue<Runnable>(MAX_TOTAL))
    }

    /**
     * Execute on a WebDriver instance in the shared object pool. It uses
     * a blocking queue under the hood, so do not expect that actions will
     * be completed immediately.
     *
     * @param action Action to execute
     */
    fun execute(action: BrowserAction) {
        executor.submit {
            var driver: JBrowserDriver? = null
            try {
                driver = browserPool.borrowObject()
                action.doOnExecutorPool(driver)
            } catch (e: Exception) {
                e.printStackTrace(System.err)
            } finally {
                driver?.let {
                    browserPool.returnObject(driver)
                }
            }
        }
    }
}