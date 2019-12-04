package com.example.springsweater.domain

import javax.persistence.*

@Entity
class Message(
      @Id
      @GeneratedValue(strategy = GenerationType.AUTO)
      val id: Long = 0,
      var text: String? = null,
      var tag: String? = null
) {
      constructor(text: String, tag: String): this(id = 0, text = text, tag = tag)
}