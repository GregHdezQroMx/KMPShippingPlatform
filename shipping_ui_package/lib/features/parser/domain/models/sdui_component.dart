import 'sdui_text_component.dart';
import 'sdui_text_input_component.dart';
import 'sdui_select_component.dart';
import 'sdui_button_component.dart';
import 'sdui_image_component.dart';
import 'sdui_card_component.dart';
import 'sdui_icon_component.dart';
import 'sdui_dummy_component.dart';

abstract class SDUIComponent {
  final String id;
  final String type;

  SDUIComponent({required this.id, required this.type});

  factory SDUIComponent.fromJson(Map<String, dynamic> json) {
    final type = json['type']?.toString() ?? 'unknown';
    switch (type) {
      case 'text':
        return SDUITextComponent.fromJson(json);
      case 'text_input':
        return SDUITextInputComponent.fromJson(json);
      case 'select':
        return SDUISelectComponent.fromJson(json);
      case 'button':
        return SDUIButtonComponent.fromJson(json);
      case 'image':
        return SDUIImageComponent.fromJson(json);
      case 'card':
        return SDUICardComponent.fromJson(json);
      case 'icon':
        return SDUIIconComponent.fromJson(json);
      default:
        return SDUIDummyComponent(id: json['id'] ?? '', type: type);
    }
  }
}
