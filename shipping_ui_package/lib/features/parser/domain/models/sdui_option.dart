class SDUIOption {
  final String value;
  final String label;

  SDUIOption({required this.value, required this.label});

  factory SDUIOption.fromJson(Map<String, dynamic> json) {
    return SDUIOption(
      value: (json['value'] ?? json['id'] ?? '').toString(),
      label: (json['label'] ?? '').toString(),
    );
  }
}
