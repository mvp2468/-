#!/usr/bin/env python3
"""
生成 JaCoCo 覆盖率柱状图
运行方式: python scripts/generate_coverage_chart.py
"""

import os
import re
import matplotlib.pyplot as plt
import matplotlib

# 设置字体
matplotlib.rcParams['font.sans-serif'] = ['Microsoft YaHei', 'DejaVu Sans']
matplotlib.rcParams['axes.unicode_minus'] = False

def parse_html_report(html_path):
    """解析 JaCoCo HTML 覆盖率报告"""
    data = {}
    
    if not os.path.exists(html_path):
        return None
    
    with open(html_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # 查找覆盖率百分比
    patterns = {
        'LINE': r'Line.*?(\d+)%',
        'BRANCH': r'Branch.*?(\d+)%',
        'METHOD': r'Method.*?(\d+)%',
        'CLASS': r'Class.*?(\d+)%',
    }
    
    # 从HTML中提取覆盖率数据
    coverage_table = re.search(r'<table[^>]*>(.*?)</table>', content, re.DOTALL)
    if coverage_table:
        table_content = coverage_table.group(1)
        
        # 提取所有百分比
        percentages = re.findall(r'(\d+)%', table_content)
        if len(percentages) >= 3:
            data['Line'] = int(percentages[0])
            data['Branch'] = int(percentages[1])
            data['Method'] = int(percentages[2])
    
    return data if data else None

def generate_chart(data, output_path):
    """生成柱状图"""
    
    fig, ax = plt.subplots(figsize=(10, 6))
    
    categories = list(data.keys())
    values = list(data.values())
    
    # 颜色根据覆盖率值变化
    def get_color(val):
        if val < 30: return '#f44336'
        elif val < 60: return '#FF9800'
        else: return '#4CAF50'
    
    colors = [get_color(v) for v in values]
    
    bars = ax.bar(categories, values, color=colors, edgecolor='black', linewidth=1.5, width=0.6)
    
    # 添加数值标签
    for bar, value in zip(bars, values):
        height = bar.get_height()
        ax.annotate(f'{value}%',
                    xy=(bar.get_x() + bar.get_width() / 2, height),
                    xytext=(0, 3),
                    textcoords="offset points",
                    ha='center', va='bottom',
                    fontsize=14, fontweight='bold')
    
    # 添加参考线
    ax.axhline(y=50, color='gray', linestyle='--', alpha=0.5, label='50% threshold')
    
    ax.set_title('JaCoCo Test Coverage Report\n(Line, Branch, Method Coverage)', 
                 fontsize=14, fontweight='bold', pad=15)
    ax.set_xlabel('Coverage Type', fontsize=12)
    ax.set_ylabel('Coverage Percentage (%)', fontsize=12)
    ax.set_ylim(0, 100)
    ax.yaxis.grid(True, linestyle='--', alpha=0.5)
    ax.set_axisbelow(True)
    ax.spines['top'].set_visible(False)
    ax.spines['right'].set_visible(False)
    
    # 添加图例
    from matplotlib.patches import Patch
    legend_elements = [
        Patch(facecolor='#f44336', label='< 30% (Low)'),
        Patch(facecolor='#FF9800', label='30-60% (Medium)'),
        Patch(facecolor='#4CAF50', label='> 60% (High)')
    ]
    ax.legend(handles=legend_elements, loc='upper right')
    
    plt.tight_layout()
    plt.savefig(output_path, dpi=150, bbox_inches='tight', facecolor='white')
    print(f"Chart saved: {output_path}")

def generate_html_report(data, output_path):
    """生成HTML报告"""
    
    html = f"""<!DOCTYPE html>
<html><head><meta charset="UTF-8">
<title>Coverage Report</title>
<style>
    body{{font-family:Arial;margin:40px;background:#f5f5f5}}
    .card{{background:white;padding:30px;border-radius:10px;box-shadow:0 2px 10px rgba(0,0,0,0.1);max-width:600px;margin:auto}}
    h1{{text-align:center;color:#333}}
    table{{width:100%;border-collapse:collapse;margin:20px 0}}
    th,td{{padding:15px;text-align:center;border-bottom:1px solid #ddd}}
    th{{background:#4CAF50;color:white}}
    .low{{color:#f44336;font-weight:bold}}
    .medium{{color:#FF9800;font-weight:bold}}
    .high{{color:#4CAF50;font-weight:bold}}
    .bar{{background:#e0e0e0;border-radius:5px;height:20px;margin:5px 0}}
    .fill{{border-radius:5px;height:20px}}
</style></head><body>
<div class="card">
<h1>JaCoCo 测试覆盖率报告</h1>
<table>
<tr><th>类型</th><th>覆盖率</th><th>等级</th></tr>
<tr><td>Line (行覆盖)</td><td class="{'low' if data['Line']<30 else 'medium' if data['Line']<60 else 'high'}">{data['Line']}%</td>
<td class="{'low' if data['Line']<30 else 'medium' if data['Line']<60 else 'high'}">{'低' if data['Line']<30 else '中' if data['Line']<60 else '高'}</td></tr>
<tr><td>Branch (分支覆盖)</td><td class="{'low' if data['Branch']<30 else 'medium' if data['Branch']<60 else 'high'}">{data['Branch']}%</td>
<td class="{'low' if data['Branch']<30 else 'medium' if data['Branch']<60 else 'high'}">{'低' if data['Branch']<30 else '中' if data['Branch']<60 else '高'}</td></tr>
<tr><td>Method (方法覆盖)</td><td class="{'low' if data['Method']<30 else 'medium' if data['Method']<60 else 'high'}">{data['Method']}%</td>
<td class="{'low' if data['Method']<30 else 'medium' if data['Method']<60 else 'high'}">{'低' if data['Method']<30 else '中' if data['Method']<60 else '高'}</td></tr>
</table>
<p style="text-align:center;color:#666">图表保存于: coverage_chart.png</p>
</div></body></html>"""
    
    with open(output_path, 'w', encoding='utf-8') as f:
        f.write(html)
    print(f"HTML Report saved: {output_path}")

if __name__ == '__main__':
    # 路径
    html_report = 'app/build/reports/jacoco/testDevDebugUnitTestCoverage/html/index.html'
    output_dir = 'app/build/reports/jacoco/testDevDebugUnitTestCoverage'
    
    # 确保目录存在
    os.makedirs(output_dir, exist_ok=True)
    
    # 解析数据
    data = parse_html_report(html_report)
    
    if data:
        print("Coverage Data:")
        for k, v in data.items():
            print(f"  {k}: {v}%")
    else:
        # 示例数据
        data = {'Line': 12, 'Branch': 8, 'Method': 15}
        print("Using demo data (run tests first):")
        print("  ./gradlew :app:testDevDebugUnitTest :app:jacocoTestReport")
    
    print(f"\nGenerating charts...")
    generate_chart(data, os.path.join(output_dir, 'coverage_chart.png'))
    generate_html_report(data, os.path.join(output_dir, 'coverage_report.html'))
