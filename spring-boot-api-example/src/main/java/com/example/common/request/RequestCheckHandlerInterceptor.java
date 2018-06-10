package com.example.common.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.common.util.RequestResponseUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.log4j.Log4j2;

/**
 * HTTPリクエストのチェックを行うHandlerInterceptor
 * @author chocolaolantan
 */
@Component
@Log4j2
public class RequestCheckHandlerInterceptor implements HandlerInterceptor
{
	/**
	 * リクエストが来た際の事前処理を行う.
	 * @param request リクエスト情報
	 * @param response レスポンス情報
	 * @param handler Controllerメソッド情報
	 * @return リクエストを受け付ける場合trueを返す
	 */
	@Override
	public boolean preHandle(
			HttpServletRequest request,
			HttpServletResponse response,
			Object handler ) throws JsonProcessingException
	{
		log.info( "アクセスURL : {}", request.getRequestURL() );
		log.info( "HTTPリクエストヘッダ : {}", RequestResponseUtils.headerToString( request ) );

		return true;
	}
}
