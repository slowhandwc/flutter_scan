import 'dart:async';

import 'package:flutter/services.dart';

class FlutterScan {
  static const MethodChannel _channel =
      const MethodChannel('com.niimbot.flutter.scan');

  static const String METHOD_SCAN = 'niimbot_scan';

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<String> scan(String title, String content) async {
    try {
      final Map<dynamic, dynamic> result = await _channel
          .invokeMethod(METHOD_SCAN, {'title': title, 'content': content});
      String code = result['code'];
      return code;
    } on PlatformException catch (e) {
      return e.details['error'];
    }
  }
}
