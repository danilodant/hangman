$(document).ready(function () {
  $('#guess-form-input').focus();
  $('#guess-form').trigger("reset")
});


$(document).on('input', '#guess-form-input', () => {
  $('#guess-form').submit();
});
