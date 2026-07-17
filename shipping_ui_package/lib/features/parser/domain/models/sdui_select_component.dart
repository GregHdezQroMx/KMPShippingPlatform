import 'sdui_component.dart';

class SDUISelectComponent extends SDUIComponent {
  final String label;
  final String? defaultValue;
  final List<Map<String, String>> options;

  SDUISelectComponent({
    required super.id,
    required super.type,
    required this.label,
    this.defaultValue,
    required this.options,
  });

  factory SDUISelectComponent.fromJson(Map<String, dynamic> json) {
    return SDUISelectComponent(
      id: json['id'] ?? '',
      type: 'select',
      label: json['label'] ?? '',
      defaultValue: json['defaultValue'],
      options: (json['options'] as List?)
              ?.map((e) => Map<String, String>.from(e))
              .toList() ??
          [],
    );
  }
}
