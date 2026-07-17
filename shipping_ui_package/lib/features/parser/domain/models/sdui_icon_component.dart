import 'sdui_component.dart';

class SDUIIconComponent extends SDUIComponent {
  final String iconName;
  final String? style;
  final String? label;

  SDUIIconComponent({
    required super.id,
    required super.type,
    required this.iconName,
    this.style,
    this.label,
  });

  factory SDUIIconComponent.fromJson(Map<String, dynamic> json) {
    return SDUIIconComponent(
      id: json['id'] ?? '',
      type: 'icon',
      iconName: json['iconName'] ?? 'help',
      style: json['style'],
      label: json['label'],
    );
  }
}
