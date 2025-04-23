package com.example.gymmanagement.data.dao

import androidx.room.*
import com.example.gymmanagement.data.model.Member
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    @Query("SELECT * FROM member")
    fun getAllMembers(): Flow<List<Member>>

    @Query("SELECT * FROM member WHERE id = :traineeId")
    suspend fun getMemberById(traineeId: Int): Member?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: Member)

    @Update
    suspend fun updateMember(member: Member)

    @Delete
    suspend fun deleteMember(member: Member)
} 