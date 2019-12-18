package com.example.springsweater.domain.dto

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Dto для распознавания Google reCAPTCHA
 *
 * @project SpringSweater
 * @author WildDisk on 07.12.2019
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class CaptchaResponseDto(
        var success: Boolean,
        @JsonAlias("error-codes")
        var errorCode: Set<String>?
)