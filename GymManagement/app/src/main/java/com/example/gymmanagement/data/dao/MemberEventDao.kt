package com.example.gymmanagement.data.dao

import androidx.room.*
import com.example.gymmanagement.data.model.MemberEvent
import com.example.gymmanagement.data.model.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberEventDao {
    @Transaction
    @Query("""
        SELECT e.* FROM events e
        INNER JOIN member_events me ON e.id = me.eventId
        WHERE me.memberId = :memberId
        ORDER BY e.date ASC
    """)
    fun getMemberEvents(memberId: String): Flow<List<EventEntity>>

    @Query("SELECT * FROM member_events WHERE memberId = :memberId AND eventId = :eventId")
    fun getMemberEvent(memberId: String, eventId: Long): Flow<MemberEvent?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemberEvent(memberEvent: MemberEvent)

    @Update
    suspend fun updateMemberEvent(memberEvent: MemberEvent)

    @Delete
    suspend fun deleteMemberEvent(memberEvent: MemberEvent)

    @Query("DELETE FROM member_events WHERE memberId = :memberId")
    suspend fun deleteAllMemberEvents(memberId: String)
} 