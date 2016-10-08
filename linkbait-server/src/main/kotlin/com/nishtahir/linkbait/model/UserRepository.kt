package com.nishtahir.linkbait.model

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity class User(var email: String = "",
                   var password: String = "",
                   @Id @GeneratedValue(strategy = GenerationType.AUTO)
                   var id: Long = 0)

@Repository
interface UserRepository : CrudRepository<User, String> {

    fun findById(@Param("id") id: Long): User?

    fun findByEmail(@Param("email") email: String): User?
}