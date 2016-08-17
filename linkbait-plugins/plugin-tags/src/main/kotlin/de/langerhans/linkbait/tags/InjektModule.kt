package de.langerhans.linkbait.tags

import uy.kohesive.injekt.api.InjektScope
import uy.kohesive.injekt.registry.default.DefaultRegistrar

/**
 * Created by nish on 7/27/16.
 */
object InjektModule {
    @Volatile var scope: InjektScope = InjektScope(DefaultRegistrar())
}
