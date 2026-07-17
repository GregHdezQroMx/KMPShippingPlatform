import 'sdui_component.dart';

class SDUICardComponent extends SDUIComponent {
  final List<SDUIComponent> children;
  final String? label;

  SDUICardComponent({
    required super.id,
    required super.type,
    required this.children,
    this.label,
  });

  factory SDUICardComponent.fromJson(Map<String, dynamic> json) {
    return SDUICardComponent(
      id: json['id'] ?? '',
      type: 'card',
      label: json['label'],
      children: (json['children'] as List?)
              ?.map((e) => SDUIComponent.fromJson(Map<String, dynamic>.from(e)))
              .toList() ??
          [],
    );
  }
}
