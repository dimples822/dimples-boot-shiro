layui.define(['table', 'jquery', 'pearView'], function (exports) {
    "use strict";

    var MOD_NAME = 'pearDimples';
    var $ = layui.jquery,
        pearView = layui.pearView;

    var windowWidth = $(window).width();

    var pearDimples = {};


    pearDimples.modal = {};

    pearDimples.modal.base = function (msg, params) {
        params = params || {};
        params.titleIcoColor = params.titleIcoColor || '#5a8bff';
        params.titleIco = params.titleIco || 'exclaimination';
        params.title = params.title || [
            '<i class="layui-icon layui-icon-' +
            params.titleIco +
            '" style="font-size:12px;background:' +
            params.titleIcoColor +
            ';display:inline-block;position:relative;top:-2px;height:21px;line-height:21px;text-align:center;width:21px;color:#fff;border-radius:50%;margin-right:12px;"></i>' +
            params.titleValue,
            'background:#fff;border:none;font-weight:bold;font-size:16px;color:#08132b;padding-top:10px;height:36px;line-height:46px;padding-bottom:0;'
        ];
        params = $.extend(
            {
                skin: 'layui-layer-admin-modal dimples-alert',
                area: [windowWidth <= 750 ? '60%' : '400px'],
                closeBtn: 0,
                shadeClose: false
            },
            params
        );
        layer.alert(msg, params);
    };

    // ----------------- 弹窗类 --------------------- //

    pearDimples.alert = {};

    function alertParams(msg, params) {
        params.time = 3000;
        params.shade = 0;
        params.btn = null;
        params.title = [
            '<i class="layui-icon layui-icon-' +
            params.titleIco +
            '" style="font-size:12px;background:' +
            params.titleIcoColor +
            ';display:inline-block;font-weight:600;position:relative;top:-2px;height:21px;line-height:21px;text-align:center;width:21px;color:#fff;border-radius:50%;margin-right:12px;"></i>' +
            (msg || '请填写提示信息'),
            'background:#fff;border:none;font-weight:500;font-size:14px;color:#08132b;margin-bottom:-50px;padding:16px;height:45px;line-height:14px;padding-bottom:0;'
        ];
        params.offset = '40px';
        params.area = [windowWidth <= 750 ? '80%' : '400px'];
    }

    pearDimples.alert.success = function (msg, params) {
        params = params || {};
        params.titleIco = 'ok';
        params.titleIcoColor = '#30d180';
        alertParams(msg, params);
        pearDimples.modal.base('', params);
    };
    pearDimples.alert.warn = function (msg, params) {
        params = params || {};
        params.titleIco = 'exclaimination';
        params.titleIcoColor = '#ffc107';
        alertParams(msg, params);
        pearDimples.modal.base('', params);
    };
    pearDimples.alert.error = function (msg, params) {
        params = params || {};
        params.titleIco = 'close';
        params.titleIcoColor = '#ff5652';
        alertParams(msg, params);
        pearDimples.modal.base('', params);
    };
    pearDimples.alert.info = function (msg, params) {
        params = params || {};
        params.titleIco = 'infomation';
        params.titleIcoColor = '#2db7f5';
        alertParams(msg, params);
        pearDimples.modal.base('', params);
    };

    // ----------------- 模态框类 --------------------- //

    pearDimples.modal.view = function (title, url, params) {
        params = $.extend({
            url: url,
            maxmin: false,
            shadeClose: false,
            title: [
                title,
                'font-size:15px;color:#08132b;line-height:46px;height:48px;padding-bottom:0;background-color:#fff;border-bottom:none'
            ],
            area: $(window).width() <= 750 ? ['80%', '80%'] : ['50%', '60%']
        }, params);
        pearDimples.popup(params);
    };

    pearDimples.modal.info = function (title, msg, yes, params) {
        params = params || {};
        params.titleIco = 'infomation';
        params.titleIcoColor = '#2db7f5';
        params.titleValue = title;
        params.shadeClose = false;
        params = $.extend({
            btn: ['确定']
            , yes: function (index, layero) {
                yes && (yes)();
                layer.close(index);
            }
        }, params);
        pearDimples.modal.base(msg, params);
    };

    pearDimples.isUrl = function (str) {
        return /^([hH][tT]{2}[pP]:\/\/|[hH][tT]{2}[pP][sS]:\/\/)(([A-Za-z0-9-~]+)\.)+([A-Za-z0-9-~\/])+$/.test(
            str
        )
    };
    pearDimples.popup = function (params) {
        var url = params.url || '';
        var success = params.success || function () {
        };
        params.skin = 'layui-layer-admin-page';
        var defaultParams = {
            type: 1,
            area: $(window).width() <= 750 ? ['90%', '90%'] : ['60%', '90%'],
            shadeClose: true
        };

        console.log("用户添加url是否是一个完整的http链接：" + pearDimples.isUrl(url));
        if (pearDimples.isUrl(url)) {
            params.type = 2;
            params.content = url;
            layer.open($.extend(defaultParams, params));
            return
        }

        pearView.tab.del(url);

        pearView.loadHtml(url, function (res) {
            var htmlElem = $('<div>' + res.html + '</div>');

            if (params.title === undefined) {
                params.title = htmlElem.find('title').text() || '信息';
                if (params.title) htmlElem.find('title').remove()
            }

            params.content = htmlElem.html();
            params.success = function (layer, index) {
                success(layer, index);

                // pearView.parse(layer);
            };

            params = $.extend(defaultParams, params);

            if (params.location != null && params.location != "") {
                params.location.layer.open($.extend(defaultParams, params));
            } else {
                layer.open($.extend(defaultParams, params));
            }
        });
    };

    // ----------------- 网络请求类 --------------------- //
    // ajax post请求
    pearDimples.post = function (url, params, success) {
        if (params) {
            params.invalidate_ie_cache = new Date();
        }
        $.post(url, params, function (r) {
            resolveResponse(r, success);
        })
    };

    function resolveResponse(r, f) {
        if (r.code === 200) {
            f(r) && (f)();
        } else if (r.code === 401) {
            pearDimples.modal.info('登录失效', '登录已失效，请重新登录', function () {
                window.location.href = ctx + 'login';
            });
        } else if (r.code === 403) {
            pearDimples.alert.warn('对不起，您暂无该操作权限');
        } else {
            pearDimples.alert.error(r.message ? r.message : '操作失败');
            console.error(r);
        }
    }

    exports(MOD_NAME, pearDimples);
});


















