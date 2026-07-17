import 'sdui_component.dart';

class SDUITextComponent extends SDUIComponent {
  final String label;
  final String? style;

  SDUITextComponent({
    required super.id,
    required super.type,
    required this.label,
    this.style,
  });

  factory SDUITextComponent.fromJson(Map<String, dynamic> json) {
    return SDUITextComponent(
      id: json['id'] ?? '',
      type: 'text',
      label: json['label'] ?? '',
      style: json['style'],
    );
  }
}
