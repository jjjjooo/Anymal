import Vue from 'vue';
import {
  extend,
  ValidationObserver,
  ValidationProvider,
} from 'vee-validate';
import {
  required,
  digits,
  numeric,
  email,
  confirmed,
  max,
  min,
} from 'vee-validate/dist/rules';

extend('limit', (value, params) => {
  const [min, max] = params;
  if ((value && value.length < min) || value.length > max) {
    return `{_field_} 필드는 ${min}자 ~ ${max}자를 초과할 수 없습니다`;
  }
  return true;
});
extend('korAlphaNum', {
  validate: value => {
    let regex = /^[가-힣|aA-zZ|0-9]*$/.test(value);
    if (!regex) {
      return '올바른 한글, 영문, 숫자만 입력해주세요.';
    } else {
      return true;
    }
  },
});

extend('password', {
  validate: value => {
    let regex =
      /^(?=.*?[a-zA-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,16}$/.test(
        value,
      );
    if (!regex) {
      return '비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용할 수 있습니다.';
    } else {
      return true;
    }
  },
});

extend('required', {
  ...required,
  message: '{_field_} 필드는 반드시 입력해야 합니다',
});

extend('required-select', {
  ...required,
  message: '{_field_} 필드는 반드시 선택해야 합니다',
});

extend('max', {
  ...max,
  message:
    '{_field_} 닉네임은 {length}자리 이하로 정해주세요.',
});
extend('min', {
  ...min,
  message:
    '{_field_} 닉네임은{length}자리 이상으로 정해주세요.',
});
extend('numeric', {
  ...numeric,
  message: '{_field_} 필드는 숫자로만 구성되어야 합니다',
});
extend('digits', {
  ...digits,
  message: '{_field_} 필드는 11자리 여야 합니다',
});

extend('email', {
  ...email,
  message: '이메일 형식에 맞춰주세요.',
});

// provider 에 :rules="{ confirmed: 'provider name 명을 적어주면 된다'}"
extend('confirmed', {
  ...confirmed,
  message: '비밀번호와 비밀번호확인이 일치하지 않습니다',
});

Vue.component('ValidationObserver', ValidationObserver);
Vue.component('ValidationProvider', ValidationProvider);
