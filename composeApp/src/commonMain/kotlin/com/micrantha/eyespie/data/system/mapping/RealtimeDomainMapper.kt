package com.micrantha.eyespie.data.system.mapping

import com.micrantha.eyespie.domain.model.RealtimeAction
import com.micrantha.eyespie.domain.model.RealtimeAction.Add
import com.micrantha.eyespie.domain.model.RealtimeAction.Modify
import com.micrantha.eyespie.domain.model.RealtimeAction.Query
import com.micrantha.eyespie.domain.model.RealtimeAction.Remove
import com.micrantha.eyespie.domain.model.Thing
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.PostgresAction.Delete
import io.github.jan.supabase.realtime.PostgresAction.Insert
import io.github.jan.supabase.realtime.PostgresAction.Select
import io.github.jan.supabase.realtime.PostgresAction.Update
import io.github.jan.supabase.realtime.decodeOldRecord
import io.github.jan.supabase.realtime.decodeRecord

class RealtimeDomainMapper {
    fun thing(action: PostgresAction): RealtimeAction<Thing> = when (action) {
        is Insert -> Add(action.decodeRecord())
        is Update -> Modify(action.decodeRecord(), action.decodeOldRecord())
        is Delete -> Remove(action.decodeOldRecord())
        is Select -> Query(action.decodeRecord())
    }
}
