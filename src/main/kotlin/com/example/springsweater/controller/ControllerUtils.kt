package com.example.springsweater.controller

import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import java.util.stream.Collector
import java.util.stream.Collectors

/**
 * Вспомогательные функции для контроллеров
 *
 * @project SpringSweater
 * @author WildDisk on 07.12.2019
 */
object ControllerUtils {
    /**
     * Возвращаение ошибок при валидации
     */
    fun getErrors(bindingResult: BindingResult): MutableMap<String, String> {
        val collectors: Collector<FieldError, *, MutableMap<String, String>> = Collectors.toMap(
                { fieldError: FieldError -> "${fieldError.field}Error" }, { obj: FieldError -> obj.defaultMessage }
        )
        return bindingResult.fieldErrors.stream().collect(collectors)
    }
}