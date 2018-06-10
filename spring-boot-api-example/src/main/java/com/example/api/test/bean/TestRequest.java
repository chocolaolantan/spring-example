package com.example.api.test.bean;

import com.example.common.request.RequestBean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Test用、リクエストBeanクラス.
 * @author chocolaolantan
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class TestRequest extends RequestBean
{
	/** テキスト文字列. */
	private String text;
}
