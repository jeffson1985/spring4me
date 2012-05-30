var userValidateOptions = {
	rules:{
		username: {
			required:true,
			minlength:10
		},
		password: {
			required:true,
			minlength:6
		}
	},
	//
	messages: {
		username: {
			required:'用户名是必填项！',
			minlength:'请输入有效的Email！'
		},
		password: {
			required:'密码是必填项！',
			minlength:'密码长度必须大于6！',
		}
	}
};