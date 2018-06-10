package com.example.common.util;

import java.util.Enumeration;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
public final class RequestResponseUtils
{
	private static ObjectMapper mapper = null;

	/**
	 * オブジェクトをJSONにパースする.
	 * @param object 変換対象インスタンス
	 * @return json文字列
	 */
	public static String toJson( Object object )
	{
		if( Objects.isNull( mapper ) ) {
			mapper = new ObjectMapper();
		}

		String json = null;

		if( Objects.isNull( object ) ) {
			return json;
		}

		try {
			json = mapper.writeValueAsString( object );
		} catch (JsonProcessingException e) {
			log.warn( "JSONへのパースに失敗しました target=[{}]", object );
		}

		return json;
	}

	/**
	 * HTTPリクエストヘッダを文字列にする.
	 * @param request HTTPリクエスト情報
	 * @return HTTPヘッダの内容
	 */
	public static String headerToString( final HttpServletRequest request )
	{
		StringBuilder sb = new StringBuilder();

		Enumeration<String> headerNames = request.getHeaderNames();

		sb.append( '{' );
		sb.append( '"' );
		sb.append( "header");
		sb.append( '"' );
		sb.append( ':' );
		sb.append( '{' );
		String name = null;
		while( headerNames.hasMoreElements() ) {
			name = headerNames.nextElement();
			sb.append( '"' );
			sb.append( name );
			sb.append( '"' );
			sb.append( ':' );
			sb.append( '"' );
			sb.append( request.getHeader( name ) );
			sb.append( '"' );
			sb.append( headerNames.hasMoreElements() ? ',' : '}' );
		}
		sb.append( '}' );

		return sb.toString();
	}
}
