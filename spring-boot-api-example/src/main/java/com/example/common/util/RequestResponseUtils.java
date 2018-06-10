package com.example.common.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.log4j.Log4j2;

@Log4j2
public final class RequestResponseUtils
{
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
