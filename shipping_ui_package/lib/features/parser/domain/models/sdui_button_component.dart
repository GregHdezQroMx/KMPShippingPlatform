import 'sdui_component.dart';

class SDUIButtonComponent extends SDUIComponent {
  final String label;
  final String? style;
  final Map<String, dynamic> action;

  SDUIButtonComponent({
    required super.id,
    required super.type,
    required this.label,
    this.style,
    required this.action,
  });

  factory SDUIButtonComponent.fromJson(Map<String, dynamic> json) {
    return SDUIButtonComponent(
      id: json['id'] ?? '',
      type: 'button',
      label: json['label'] ?? '',
      style: json['style'],
      action: Map<String, dynamic>.from(json['action'] ?? {}),
    );
  }
}
