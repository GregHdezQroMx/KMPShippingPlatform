import 'sdui_component.dart';

class SDUITextInputComponent extends SDUIComponent {
  final String label;
  final String inputType;
  final String? defaultValue;
  final List<Map<String, dynamic>>? validations;

  SDUITextInputComponent({
    required super.id,
    required super.type,
    required this.label,
    this.inputType = 'text',
    this.defaultValue,
    this.validations,
  });

  factory SDUITextInputComponent.fromJson(Map<String, dynamic> json) {
    return SDUITextInputComponent(
      id: json['id'] ?? '',
      type: 'text_input',
      label: json['label'] ?? '',
      inputType: json['inputType'] ?? 'text',
      defaultValue: json['defaultValue'],
      validations: (json['validations'] as List?)?.map((e) => Map<String, dynamic>.from(e)).toList(),
    );
  }
}
