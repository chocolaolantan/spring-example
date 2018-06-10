package com.example.common.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;

import com.example.common.util.RequestResponseUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

/**
 * リクエスト情報を1つのBeanに変換する.
 * @author chocolaolantan
 */
@Component
@Log4j2
public class RequestConvertProcessor extends ModelAttributeMethodProcessor
{
	private ObjectMapper mapper;

	public RequestConvertProcessor()
	{
		super( true );
		mapper = new ObjectMapper();
	}

	/**
	 * リクエスト情報の書換え対象かどうかを確認する.
	 * {@inheritDoc}
	 */
	@Override
	public boolean supportsParameter( MethodParameter parameter )
	{
		return RequestBean.class.isAssignableFrom( parameter.getParameterType() );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void bindRequestParameters( WebDataBinder binder, NativeWebRequest request )
	{
		binder.bind( new MutablePropertyValues( request.getParameterMap() ) );
	}

	/**
	 * リクエスト情報をBeanにマッピングする.
	 * {@inheritDoc}
	 */
	@Override
	protected Object constructAttribute(
			Constructor<?> requestModelConstructor,
			String attributeName,
			WebDataBinderFactory binderFactory,
			NativeWebRequest webRequest) throws JsonParseException, JsonMappingException, IOException
	{
		HttpServletRequest request = webRequest.getNativeRequest( HttpServletRequest.class );

		RequestBean requestModel = null;

		String method = request.getMethod();
		String contentType = request.getContentType();

		// リクエストメソッドに応じて振り分け
		if( HttpMethod.GET.toString().equals( method ) ) {
			requestModel = createGetRequestBean( requestModelConstructor, request );
		}
		else if( HttpMethod.POST.toString().equals( method ) ) {
			requestModel = createPostRequestBean( requestModelConstructor, request );
		}

		log.info( "HTTPリクエストボディ : {\"body\":{}}", RequestResponseUtils.toJson( requestModel ) );

		return requestModel;
	}

	/**
	 * GETリクエストのパースをする.
	 * @param requestModelConstructor リクエストBeanのコンストラクタ
	 * @param request リクエスト情報
	 * @return リクエストBean
	 */
	private RequestBean createGetRequestBean(Constructor<?> requestModelConstructor, HttpServletRequest request)
	{
		Object requestModel = null;

		try {
			requestModel = requestModelConstructor.newInstance( ( Object[] ) null );
			Map<String, String[]> map = request.getParameterMap();
			for( Field field : requestModelConstructor.getDeclaringClass().getDeclaredFields() ) {
				if( Objects.isNull( map.get( field.getName() ) ) ) {
					continue;
				}
				field.setAccessible( true );
				setValue( field, requestModel, map );
			}
		} catch (IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException | ParseException e) {
			e.printStackTrace();
		}

		return setupRequest( request, ( RequestBean ) requestModel );
	}

	/**
	 * POSTリクエストのパースをする.
	 * @param requestModelConstructor リクエストBeanのコンストラクタ
	 * @param request リクエスト情報
	 * @return リクエストBean
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private RequestBean createPostRequestBean(
			Constructor<?> requestModelConstructor,
			HttpServletRequest request )
					throws JsonParseException, JsonMappingException, IOException
	{
		ServletInputStream inputStream = request.getInputStream();
		StringBuilder sb = new StringBuilder();

		try( BufferedReader br = new BufferedReader( new InputStreamReader( inputStream ) ) ) {
			String line = null;
			while( Objects.nonNull( line = br.readLine() ) ) {
				sb.append( line );
			}
		}

		RequestBean requestModel = ( RequestBean ) mapper.readValue(sb.toString(), requestModelConstructor.getDeclaringClass() );
		return setupRequest( request, requestModel );
	}

	/**
	 * メタデータなどをセットする.
	 * @param request リクエスト情報
	 * @param requestModel リクエストBean
	 * @return リクエストBean
	 */
	private RequestBean setupRequest( HttpServletRequest request, RequestBean requestModel )
	{
		requestModel.setClient( request.getHeader( "client" ) );
		requestModel.setMethod( request.getMethod() );
		requestModel.setUserAgent( request.getHeader( HttpHeaders.USER_AGENT ) );
		requestModel.setReceptionDate( Date.from( Instant.now() ) );

		return requestModel;
	}

	/**
	 * 与えられたFieldにMapから取得した値をセットする.
	 * @param field フィールド情報
	 * @param requestModel 値をセットするオブジェクト
	 * @param map 値を保持するMap
	 * @throws IllegalArgumentException 値のセットに失敗した時にスローする
	 * @throws IllegalAccessException Fieldアクセスに失敗した時にスローする
	 * @throws ParseException 型変換に失敗した時にスローする
	 */
	private void setValue(Field field, Object requestModel, Map<String, String[]> map)
			throws IllegalArgumentException, IllegalAccessException, ParseException
	{
		String[] values = map.get( field.getName() );

		if( Objects.isNull( values ) ) {
			return;
		}
		else  {
			field.setAccessible( true );

			if( values.length != 1 ) {
				field.set( requestModel, values );
			}
			// 型変換を実施
			else {

				String value = values[0];
				if( String.class.isAssignableFrom( field.getDeclaringClass() ) ) {
					field.set( requestModel, value );
				}
				else if( Integer.class.isAssignableFrom( field.getDeclaringClass() )
						|| int.class.isAssignableFrom( field.getDeclaringClass() ) ) {
					field.set( requestModel, Integer.parseInt( value ) );
				}
				else if( Long.class.isAssignableFrom( field.getDeclaringClass() )
						|| long.class.isAssignableFrom( field.getDeclaringClass() ) ) {
					field.set( requestModel, Long.parseLong( value ) );
				}
				else if( Float.class.isAssignableFrom( field.getDeclaringClass() )
						|| float.class.isAssignableFrom( field.getDeclaringClass() ) ) {
					field.set( requestModel, Float.parseFloat( value ) );
				}
				else if( Double.class.isAssignableFrom( field.getDeclaringClass() )
						|| double.class.isAssignableFrom( field.getDeclaringClass() ) ) {
					field.set( requestModel, Double.parseDouble( value ) );
				}
				else if( Date.class.isAssignableFrom( field.getDeclaringClass() ) ) {
					String dateFormat = null;
					if( value.length() == 8 ) {
						dateFormat = "yyyyMMdd";
					} else if( value.length() == 14 ) {
						dateFormat = "yyyyMMddHHmmss";
					}
					SimpleDateFormat sdf = new SimpleDateFormat( dateFormat );
					field.set( requestModel, sdf.parse( value ) );
				}
			}
		}
	}
}
