package com.example.common.response;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.log4j.Log4j2;

/**
 * 例外ハンドリングを行う.
 * @author chocolaolantan
 */
@ControllerAdvice
@Log4j2
public class ResponseHandlerInterceptor
{
	/**
	 * 予期しない例外のハンドリングを行う.
	 * @param t 例外
	 * @return 例外発生時用レスポンス
	 */
	@ExceptionHandler
	public ExceptionResponseBean handleThrowable( Throwable t )
	{
		List<String> errors = new ArrayList<>();
		errors.add( t.getMessage() );

		ExceptionResponseBean response = new ExceptionResponseBean( t.getMessage(), errors );

		log.error( "予期せぬエラー発生", t );

		return response;
	}
}
