package com.example.techshopserver.users

import com.example.techshopserver.users.responses.UserResponse
import jakarta.persistence.*
import jakarta.validation.constraints.Email

@Entity
@Table(name = "TblUser")
class User(
    @Id @GeneratedValue
    var id: Long? = null,

    @Email
    @Column(unique = true, nullable = false)
    var email: String = "",

    @Column(length = 50)
    var password: String = "",

    @Column(nullable = false)
    var name: String = "",

    @ManyToMany
    @JoinTable(
        name = "UserRole",
        joinColumns = [JoinColumn(name = "idUser")],
        inverseJoinColumns = [JoinColumn(name = "idRole")]
    )
    val roles: MutableSet<Role> = mutableSetOf()
) {
    fun toResponse() = UserResponse(
        name = this.name,
        id = this.id!!,
        email = this.email
    )
}