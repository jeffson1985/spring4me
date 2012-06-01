var userValidateOptions = {
	rules:{
		password: {
			min:'6',
			notempty:true
		},
		username: {
			notempty:true,
			email:true
		}
	},
	messages: {
		password: {
			min:'must be greater than or equal to 6',
			notempty:'may not be empty'
		},
		username: {
			notempty:'may not be empty',
			email:'not a well-formed email address'
		}
	}
};