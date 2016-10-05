package com.nishtahir.linkbait.plugin.common

import com.machinepublishers.jbrowserdriver.JBrowserDriver
import com.machinepublishers.jbrowserdriver.Settings
import com.machinepublishers.jbrowserdriver.Timezone
import com.machinepublishers.jbrowserdriver.UserAgent
import org.apache.commons.pool2.BasePooledObjectFactory
import org.apache.commons.pool2.PooledObject
import org.apache.commons.pool2.PooledObjectFactory
import org.apache.commons.pool2.impl.DefaultPooledObject
import org.apache.commons.pool2.impl.GenericObjectPool
import org.apache.commons.pool2.impl.GenericObjectPoolConfig

class BrowserFactory : BasePooledObjectFactory<JBrowserDriver>() {

    override fun create(): JBrowserDriver? {
        return JBrowserDriver(Settings.builder().
                userAgent(UserAgent.CHROME).
                timezone(Timezone.AMERICA_NEWYORK).build())
    }

    override fun wrap(obj: JBrowserDriver?): PooledObject<JBrowserDriver>? {
        return DefaultPooledObject<JBrowserDriver>(obj);
    }

    override fun passivateObject(pooledObject: PooledObject<JBrowserDriver>?) {
        pooledObject?.`object`?.reset()
    }

}

class BrowserPool(factory: PooledObjectFactory<JBrowserDriver>?,
                  config: GenericObjectPoolConfig?) : GenericObjectPool<JBrowserDriver>(factory, config) {
}