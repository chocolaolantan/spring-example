package com.example.common.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 本APIへアクセスする際、一般にこれを継承し、ユーザ認証を経る.
 * @author chocolaolantan
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class TokenRequestBean extends RequestBean
{
	/** ユーザID. */
	private String userId;
}
