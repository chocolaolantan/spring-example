package com.example.common.response;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Objects;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.common.annotation.NoStatusResponse;
import com.example.common.util.RequestResponseUtils;

import lombok.extern.log4j.Log4j2;

/**
 * すべてのレスポンスに共通処理を施す.
 * <ul>
 * <li>処理結果の設定</li>
 * </ul>
 * @author chocolaolantan
 */
@RestControllerAdvice
@Log4j2
public class ResponseConvertProcessor implements ResponseBodyAdvice<Object>
{
	/**
	 * HandlerMethodの戻り値が、共通処理の対象かどうかを判定する.
	 * <br />
	 * ※今回はすべてのHandlerMethodを対象にしている
	 * @param returnType HandlerMethodの戻り値型情報
	 * @param converterType 選択されている変換型
	 * @return trueなら共通処理の対象
	 */
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType)
	{
		return true;
	}

	/**
	 * ResponseBodyへの書込み対象を編集する.
	 * @param body 書込まれる予定だったオブジェクト
	 * @param returnType ControllerMethodの戻り値情報
	 * @param selectedContentType
	 * @param selectedConverterType
	 * @param request 現在のリクエスト情報
	 * @param response 現在のレスポンス情報
	 * @return 成形した書込みオブジェクト
	 */
	@Override
	public Object beforeBodyWrite(
			Object body,
			MethodParameter returnType,
			MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request,
			ServerHttpResponse response)
	{
		Annotation[] annotations = returnType.getMethodAnnotations();

		// 処理結果付与対象外アノテーションの確認
		if ( contains( annotations, NoStatusResponse.class ) ) {
		}
		// 戻り値がない場合、処理結果成功を設定する
		else if (void.class.isAssignableFrom(returnType.getParameterType())) {
			ResponseBean res = new ResponseBean();
			res.setStatus(ResponseStatus.SUCCESS);
			body = res;
		}
		// 例外レスポンスを返却する際、処理結果失敗を設定する
		else if (ExceptionResponseBean.class.isAssignableFrom(body.getClass())) {
			((ExceptionResponseBean) body).setStatus(ResponseStatus.FAIL);
		}
		// 通常レスポンスを返却する際、処理結果成功を設定する
		else if (ResponseBean.class.isAssignableFrom(body.getClass())) {
			((ResponseBean) body).setStatus(ResponseStatus.SUCCESS);
		}

		log.info( "レスポンス内容 : {}", RequestResponseUtils.toJson( body ) );

		return body;
	}

	/**
	 * 配列内に指定したクラスのオブジェクトが含まれているかを確認する.
	 * @param array 検索対象配列
	 * @param clazz 検索対象
	 * @return 対象が含まれていればtrueを返す
	 */
	private <T> boolean contains( T[] array, Class<?> clazz )
	{
		if( Objects.isNull( array ) || array.length <= 0 ) {
			return false;
		}

		if( Arrays.asList( array ).stream().anyMatch( o -> clazz.isAssignableFrom( o.getClass() ) )) {
			return true;
		}

		return false;
	}
}
