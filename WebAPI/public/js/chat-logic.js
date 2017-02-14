(function() {
    var title = $('.title').text();
    var admin = title.split('-')[0];
    var user = title.split('-')[1];
    var socket = io();
    socket.emit('restore messages', {
        admin: admin,
        user: user
    });
    var Message;
    Message = function(arg) {
        this.text = arg.text, this.message_side = arg.message_side;
        this.draw = function(_this) {
            return function() {
                var $message;
                $message = $($('.message_template').clone().html());
                $message.addClass(_this.message_side).find('.text').html(_this.text);
                $('.messages').append($message);
                return setTimeout(function() {
                    return $message.addClass('appeared');
                }, 0);
            };
        }(this);
        return this;
    };
    $(function() {
        var getMessageText, message_side, sendMessage;
        message_side = 'right';
        getMessageText = function() {
            var $message_input;
            $message_input = $('.message_input');
            return $message_input.val();
        };
        sendMessage = function(text, author) {
            var $messages, message;
            if (text.trim() === '') {
                return;
            }
            $('.message_input').val('');
            $messages = $('.messages');
            message_side = author === user ? 'right' : 'left';
            message = new Message({
                text: text,
                message_side: message_side
            });
            message.draw();
            return $messages.animate({ scrollTop: $messages.prop('scrollHeight') }, 300);
        };
        $('.send_message').click(function(e) {
            socket.emit('chat message', {
                author: user,
                admin: admin,
                user: user,
                message: getMessageText()
            });
        });
        $('.message_input').keyup(function(e) {
            if (e.which === 13) {
                socket.emit('chat message', {
                    author: user,
                    admin: admin,
                    user: user,
                    message: getMessageText()
                });
            }
        });
        socket.on('restore messages ' + admin + ' ' + user, function(msgs) {
            for (let i = 0; i < msgs.length; i++) {
                sendMessage(msgs[i].message, msgs[i].author);
            }
            return;
        });
        socket.on('chat message ' + admin + ' ' + user, function(result) {
            return sendMessage(result.message, result.author);
        });
    });
}.call(this));