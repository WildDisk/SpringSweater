package com.example.springsweater.domain

import javax.persistence.*

/**
 * @project SpringSweater
 * @author WildDisk
 */
@Entity
class Message(
      @Id
      @GeneratedValue(strategy = GenerationType.AUTO)
      val id: Long = 0,
      var text: String? = null,
      var tag: String? = null,
      @ManyToOne(fetch = FetchType.EAGER)
      @JoinColumn(name = "usr_id")
      var author: User? = null,
      var filename: String? = null
) {
      constructor(text: String, tag: String): this(id = 0, text = text, tag = tag)
      constructor(text: String, tag: String, user: User): this(id = 0, text = text, tag = tag, author = user)
      fun getAuthorName(): String = author?.username ?: "{none}"
}