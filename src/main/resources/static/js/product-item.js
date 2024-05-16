$(document).ready(function () {

  var jAmountInput = $('#jAmount');
  var increaseBtn = $('#jAmountIncreaseBtn');
  var decreaseBtn = $('#jAmountDecreaseBtn');

  // jAmount 输入框的输入事件处理
  jAmountInput.on('input', function () {
    var value = $(this).val().replace(/\D/g, '');
    $(this).val(value);
  });

  // 增加按钮点击事件处理
  increaseBtn.on('click', function () {
    var currentValue = parseInt(jAmountInput.val()) || 0;
    jAmountInput.val(currentValue + 1);
  });

  // 减少按钮点击事件处理
  decreaseBtn.on('click', function () {
    var currentValue = parseInt(jAmountInput.val()) || 2;
    if (currentValue > 1) {
      jAmountInput.val(currentValue - 1);
    }
  });
});
